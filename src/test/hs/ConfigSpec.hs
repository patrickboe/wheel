{-# LANGUAGE OverloadedStrings #-}
module ConfigSpec (spec) where

import Config
import Test.Hspec
import Auth (AuthConfig (..))

spec :: Spec
spec = do
  describe "Config.loadConfig" $ do
    it "fails on missing files" $ do
      loadConfig "web.missing.cfg" `shouldThrow` anyException
    it "finds all configured values" $ do
      loadConfig "web.example.cfg" `shouldReturn` AuthConfig { privatekey="hi mom"
      , audience="me"
      , issuer="you" }
