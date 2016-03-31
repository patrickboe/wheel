{-# LANGUAGE OverloadedStrings #-}
module Api (startApp) where

import Users
import Auth
import Network.Wai
import Network.Wai.Handler.Warp
import Servant
import Config

type API = "user"
        :> Header "Authorization" AuthHeader
        :> Capture "username" String
        :> ReqBody '[JSON] User
        :> Put '[JSON] ()

api :: Proxy API
api = Proxy

startApp :: IO ()
startApp = run 3001 . app =<< loadConfig "web.cfg"

app :: AuthConfig -> Application
app cfg =
  serve api server
  where
  authenticate = authentication cfg
  authenticated e h x y = authenticate h >>= e x y
  server = authenticated register
