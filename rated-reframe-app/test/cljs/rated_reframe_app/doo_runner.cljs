(ns rated-reframe-app.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [rated-reframe-app.core-test]))

(doo-tests 'rated-reframe-app.core-test)

