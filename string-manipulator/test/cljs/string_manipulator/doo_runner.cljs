(ns string-manipulator.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [string-manipulator.core-test]))

(doo-tests 'string-manipulator.core-test)

