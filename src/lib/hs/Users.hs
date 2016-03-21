module Users where

import GHC.Generics
import Servant
import Control.Monad.Trans.Either
import Data.Aeson

data User = User
  { name :: String } deriving Generic

instance ToJSON User
instance FromJSON User

register :: User -> EitherT ServantErr IO ()
register _ = return ()
