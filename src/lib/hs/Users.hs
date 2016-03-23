module Users where

import GHC.Generics
import Servant
import Id
import Control.Monad.Trans.Either
import Data.Aeson

data User = User
  { email :: String } deriving Generic

instance ToJSON User
instance FromJSON User

register :: String
         -> User
         -> Identity
         -> EitherT ServantErr IO ()
register _ _ _ = return ()
