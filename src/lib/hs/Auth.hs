{-# LANGUAGE OverloadedStrings #-}
module Auth where

import Data.Bifunctor
import Control.Monad
import Servant
import Data.ByteString.Lazy
import GHC.Generics
import Web.JWT
import Id
import Control.Monad.Trans.Either
import qualified Data.Text as T

newtype AuthHeader = AuthHeader T.Text
  deriving (Eq, Generic)

instance FromText AuthHeader where
  fromText = Just . AuthHeader

data AuthConfig = AuthConfig { privatekey :: T.Text
                             , audience :: T.Text
                             , issuer :: T.Text
                             } deriving (Eq, Show)

bounce :: Monad m => ByteString -> EitherT ServantErr m a
bounce msg = left $ err401 { errBody = msg }

authentication :: Monad m
               => AuthConfig
               -> Maybe AuthHeader
               -> EitherT ServantErr m Identity
authentication _ Nothing = bounce "Authorization header missing"
authentication (AuthConfig k a i) (Just (AuthHeader header)) =
  parseBearer header >>= parseJwt >>= validateClaims >>= obtainSubject
  where
        key = secret k

        parseBearer h = case T.words h of
            [] -> bounce "empty Authorization header"
            "bearer" : [t] -> return t
            _ -> bounce "malformed Authorization header"

        parseJwt t = case decodeAndVerifySignature key t of
            Nothing -> bounce "invalid token"
            Just vt -> return $ claims vt

        validateIssuer cs = case fmap stringOrURIToText $ iss cs of
            Just x | x == i -> return cs
            _  -> bounce "incorrect token issuer"

        validateAudience cs = case fmap (bimap stringOrURIToText id) $ aud cs of
            Just (Left x) | x == a -> return cs
            _ -> bounce "incorrect token audience"

        validateClaims = validateIssuer >=> validateAudience

        obtainSubject cs = case sub cs of
            Nothing -> bounce "token has no subject"
            Just s -> return $ Identity $ stringOrURIToText s

