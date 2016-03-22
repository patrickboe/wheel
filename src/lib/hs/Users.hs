module Users where

import GHC.Generics
import Servant
import Control.Monad.Trans.Either
import Data.Aeson
import qualified Data.Text as T

data User = User
  { email :: String } deriving Generic

data Identity = Identity
  { name :: String } deriving Eq

newtype AuthHeader = AuthHeader String
  deriving (Eq, Generic)

instance FromText AuthHeader where
  fromText x = Just $ AuthHeader $ T.unpack x

instance ToJSON User
instance FromJSON User

authenticated :: (a -> b -> Identity -> EitherT ServantErr IO c)
              -> Maybe AuthHeader
              -> a
              -> b
              -> EitherT ServantErr IO c
authenticated e h x y = authenticate h >>= e x y

authenticate :: Maybe AuthHeader
             -> EitherT ServantErr IO Identity
authenticate _ = return $ Identity "me"

register :: String
         -> User
         -> Identity
         -> EitherT ServantErr IO ()
register _ _ _ = return ()
