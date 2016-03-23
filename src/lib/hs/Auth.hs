module Auth where

import Servant
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

authenticate :: Monad m
             => Maybe AuthHeader
             -> EitherT ServantErr m Identity
authenticate _ = return $ Identity "me"
