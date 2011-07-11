;  Copyright (c) Dave Ray, 2011. All rights reserved.

;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this 
;   distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns seesaw.test.invoke
  (:use seesaw.invoke)
  (:use [lazytest.describe :only (describe it testing)]
        [lazytest.expect :only (expect)]))

(describe invoke-now
  (it "should execute code on the swing thread, wait, and return the result"
    (invoke-now (javax.swing.SwingUtilities/isEventDispatchThread))))

(describe signaller
  (it "should not invoke a call if one is already in flight"
    (let [call-count (atom 0)
          signal     (signaller #(swap! % inc))]
      ; Schedule  some signals and check that only the first is queued.
      (expect (= [true false false] (invoke-now [(signal call-count) (signal call-count) (signal call-count)])))
      ; Now check the call count and make sure only one function was queued
      ; Use invoke-now so we know the deref is executed *after* the functions
      ; are processed.
      (expect (= 1 (invoke-now @call-count))))))

