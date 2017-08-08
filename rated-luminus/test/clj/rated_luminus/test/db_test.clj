(ns rated-luminus.test.db_test
  (:require [expectations :as expect ]
            [rated-luminus.db.core :refer :all]))

(expect/expect "done" (make-to-do))
