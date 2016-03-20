{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE QuasiQuotes       #-}
module Users where

import           Foundation
import           Yesod.Core
import qualified Network.HTTP.Types as H

postRegR :: Handler TypedContent
postRegR = do
  sendStatusJSON H.status201 $ object ["id" .= userid]
    where userid = 1 :: Int
