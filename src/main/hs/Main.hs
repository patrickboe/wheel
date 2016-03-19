import Application () -- for YesodDispatch instance
import Foundation
import Yesod.Core

main :: IO ()
main = warp 3001 App
