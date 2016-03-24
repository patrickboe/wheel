{-# LANGUAGE OverloadedStrings #-}
module AuthSpec (spec) where

import Auth
import Test.Hspec
import Servant.Server
import Control.Monad.Trans.Either
import Data.ByteString.Lazy
import Web.JWT
import Id
import Data.Text as T
import qualified Control.Monad.Identity as MI

bad :: ByteString -> EitherT ServantErr MI.Identity Identity
bad msg =
  left $ err401 { errBody = msg }

good :: Text -> EitherT ServantErr MI.Identity Identity
good = return . Identity

singleAud :: Text -> Maybe (Either StringOrURI [StringOrURI])
singleAud = (fmap Left) . stringOrURI

jwt :: JSON
jwt = encodeSigned HS256 key claimset
  where key = secret "my-secret-key"
        claimset = def { iss = stringOrURI "wheel"
                       , aud = singleAud "wheel"
                       , sub = stringOrURI "me" }

jwtFromJoeShmo :: JSON
jwtFromJoeShmo = encodeSigned HS256 key claimset
  where key = secret "my-secret-key"
        claimset = def { iss = stringOrURI "Joe Shmo"
                       , aud = singleAud "wheel"
                       , sub = stringOrURI "her" }

jwtForJoeShmo :: JSON
jwtForJoeShmo = encodeSigned HS256 key claimset
  where key = secret "my-secret-key"
        claimset = def { iss = stringOrURI "wheel"
                       , aud = singleAud "Joe Shmo"
                       , sub = stringOrURI "her" }

herjwt :: JSON
herjwt = encodeSigned HS256 key claimset
  where key = secret "my-secret-key"
        claimset = def { iss = stringOrURI "wheel"
                       , aud = singleAud "wheel"
                       , sub = stringOrURI "her" }

spec :: Spec
spec = do
  describe "Auth.authenticate" $ do

    it "rejects missing headers" $ do
      authenticate Nothing `shouldBe` bad "Authorization header missing"

    it "rejects empty headers" $ do
      auth "" `shouldBe` bad "empty Authorization header"

    it "rejects malformed headers" $ do
      auth "bad" `shouldBe` bad "malformed Authorization header"
      auth "bearer" `shouldBe` bad "malformed Authorization header"
      auth "bearer of bad news" `shouldBe` bad "malformed Authorization header"

    it "accepts a good token" $ do
      auth (T.append "bearer " jwt) `shouldBe` good "me"
      auth (T.append "bearer " herjwt) `shouldBe` good "her"

    it "rejects a token with the wrong issuer" $ do
      auth (T.append "bearer " jwtFromJoeShmo)
      `shouldBe`
      bad "incorrect token issuer"

    it "rejects a token with the wrong audience" $ do
      auth (T.append "bearer " jwtForJoeShmo)
      `shouldBe`
      bad "incorrect token audience"

    it "rejects a bad token" $ do
      auth "bearer xyz" `shouldBe` bad "invalid token"
  where auth = authenticate . Just . AuthHeader
