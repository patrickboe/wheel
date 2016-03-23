{-# LANGUAGE OverloadedStrings #-}
module Auth where

import Servant
import Data.ByteString.Lazy
import GHC.Generics
import Id
import Control.Monad.Trans.Either
import qualified Data.Text as T

newtype AuthHeader = AuthHeader String
  deriving (Eq, Generic)

instance FromText AuthHeader where
  fromText x = Just $ AuthHeader $ T.unpack x

authenticated :: (a -> b -> Identity -> EitherT ServantErr IO c)
              -> Maybe AuthHeader
              -> a
              -> b
              -> EitherT ServantErr IO c
authenticated e h x y = authenticate h >>= e x y

unauth :: Monad m => ByteString -> EitherT ServantErr m a
unauth msg = left $ err401 { errBody = msg }

authenticate :: Monad m
             => Maybe AuthHeader
             -> EitherT ServantErr m Identity
authenticate Nothing = unauth "Authorization header missing"
authenticate (Just (AuthHeader header)) =
  parseBearer header >>= parseJwt
  where
        parseBearer h = case words h of
            [] -> unauth "empty Authorization header"
            "bearer" : [t] -> return t
            _ -> unauth "malformed Authorization header"
        parseJwt t = return $ Identity "me"
