{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE TemplateHaskell   #-}
{-# LANGUAGE ViewPatterns      #-}
module Application where

import Foundation
import Yesod.Core
import Users

mkYesodDispatch "App" resourcesApp
