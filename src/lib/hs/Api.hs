module Api (startApp) where

import Users
import Auth
import Network.Wai
import Network.Wai.Handler.Warp
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

server :: Server API
server = authenticated register
