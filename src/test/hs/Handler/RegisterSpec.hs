module Handler.RegisterSpec (spec) where

import TestImport

spec :: Spec
spec = withApp $ do
  it "returns a 201 Created with the new entity" $ do
    post RegR
    statusIs 201
