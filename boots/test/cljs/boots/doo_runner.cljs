(ns boots.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [boots.core-test]))

(doo-tests 'boots.core-test)

