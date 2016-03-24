module Id where

import Data.Text

data Identity = Identity
  { name :: Text } deriving (Eq, Show)

