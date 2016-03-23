{-# LANGUAGE OverloadedStrings #-}
module AuthSpec (spec) where

import Auth
import Test.Hspec
import Servant.Server
import Control.Monad.Trans.Either
import Id
import qualified Control.Monad.Identity as MI

missingHeader :: EitherT ServantErr MI.Identity Identity
missingHeader =
  left $ err401 { errBody = "Authorization header missing" }

emptyHeader :: EitherT ServantErr MI.Identity Identity
emptyHeader =
  left $ err401 { errBody = "empty Authorization header" }

malformedHeader :: EitherT ServantErr MI.Identity Identity
malformedHeader =
  left $ err401 { errBody = "malformed Authorization header" }

goodId :: String -> EitherT ServantErr MI.Identity Identity
goodId n = return $ Identity n

spec :: Spec
spec = do
  describe "Auth.authenticate" $ do
    it "rejects missing headers" $ do
      authenticate Nothing `shouldBe` missingHeader
    it "rejects empty headers" $ do
      authenticate (jh "") `shouldBe` emptyHeader
    it "rejects malformed headers" $ do
      authenticate (jh "bad") `shouldBe` malformedHeader
      authenticate (jh "bearer") `shouldBe` malformedHeader
      authenticate (jh "bearer of bad news") `shouldBe` malformedHeader
    it "accepts a good token" $ do
      authenticate (jh "bearer bleh") `shouldBe` goodId "me"
  where jh s = Just $ AuthHeader $ s
