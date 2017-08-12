(ns pinned-notes.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [pinned-notes.core-test]))

(doo-tests 'pinned-notes.core-test)

