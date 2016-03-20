module TestImport
    ( module TestImport
    , module X
    ) where

import Application           ()
import ClassyPrelude         as X
import Foundation            as X
import Test.Hspec            as X
import Yesod.Test            as X

withApp :: SpecWith (TestApp App) -> Spec
withApp = before $ return (App, Prelude.id)
