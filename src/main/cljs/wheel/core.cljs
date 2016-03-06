(ns wheel.core
  (:require
    [goog.dom :as gdom]
    [om.next :as om :refer-macros [defui]]
    [om.dom :as dom]
    [wheel.fifth :as wf :refer [five]]))

(defui HelloWorld
  Object
  (render [this]
    (dom/div nil
             (str "Yeaello world! No. " five))))

(def hello (om/factory HelloWorld))

(js/ReactDOM.render (hello) (gdom/getElement "app"))
