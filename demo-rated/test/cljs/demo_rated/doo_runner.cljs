(ns demo-rated.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [demo-rated.core-test]))

(doo-tests 'demo-rated.core-test)

