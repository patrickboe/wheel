{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE QuasiQuotes       #-}
module Users where

import Foundation
import Yesod.Core

postRegR :: Handler TypedContent
postRegR = selectRep $ provideJson $ object ["id" .= userid]
    where userid = 1 :: Int
