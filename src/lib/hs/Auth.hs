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

authenticated :: (a -> b -> Identity -> EitherT ServantErr IO c)
              -> Maybe AuthHeader
              -> a
              -> b
              -> EitherT ServantErr IO c
authenticated e h x y = authenticate h >>= e x y

bounce :: Monad m => ByteString -> EitherT ServantErr m a
bounce msg = left $ err401 { errBody = msg }

key = secret "my-secret-key"

authenticate :: Monad m
             => Maybe AuthHeader
             -> EitherT ServantErr m Identity
authenticate Nothing = bounce "Authorization header missing"
authenticate (Just (AuthHeader header)) =
  parseBearer header >>= parseJwt >>= validateClaims >>= obtainSubject
  where

        parseBearer h = case T.words h of
            [] -> bounce "empty Authorization header"
            "bearer" : [t] -> return t
            _ -> bounce "malformed Authorization header"

        parseJwt t = case decodeAndVerifySignature key t of
            Nothing -> bounce "invalid token"
            Just vt -> return $ claims vt

        validateIssuer cs = case fmap stringOrURIToText $ iss cs of
            Just "wheel" -> return cs
            _  -> bounce "incorrect token issuer"

        validateAudience cs = case fmap (bimap stringOrURIToText id) $ aud cs of
            Just (Left "wheel") -> return cs
            _ -> bounce "incorrect token audience"

        validateClaims = validateIssuer >=> validateAudience

        obtainSubject cs = case sub cs of
            Nothing -> bounce "token has no subject"
            Just s -> return $ Identity $ stringOrURIToText s

