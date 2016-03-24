{-# LANGUAGE OverloadedStrings #-}
module Api (startApp) where

import Users
import Auth
import Network.Wai
import Control.Monad.Trans.Either
import Network.Wai.Handler.Warp
import Id
import Servant

type API = "user"
        :> Header "Authorization" AuthHeader
        :> Capture "username" String
        :> ReqBody '[JSON] User
        :> Put '[JSON] ()

startApp :: IO ()
startApp = run 3001 app

app :: Application
app = serve api server

api :: Proxy API
api = Proxy

authenticate :: Maybe AuthHeader
             -> EitherT ServantErr IO Identity
authenticate = authentication $ AuthConfig "what" "me" "worry"

authenticated :: (a -> b -> Identity -> EitherT ServantErr IO c)
              -> Maybe AuthHeader
              -> a
              -> b
              -> EitherT ServantErr IO c
authenticated e h x y = authenticate h >>= e x y

server :: Server API
server = authenticated register
