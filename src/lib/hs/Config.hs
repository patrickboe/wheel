{-# LANGUAGE OverloadedStrings #-}
module Config where

import Data.Configurator
import Auth

loadConfig :: Worth FilePath -> IO AuthConfig
loadConfig path = do
  cfg <- load [path]
  s <- require cfg "auth0_secret"
  i <- require cfg "auth0_issuer"
  a <- require cfg "auth0_audience"
  return $ AuthConfig { privatekey = s
                      , audience = a
                      , issuer = i}
