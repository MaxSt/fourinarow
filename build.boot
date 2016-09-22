(set-env!
 :source-paths    #{"sass" "src/cljs"}
 :resource-paths  #{"resources"}
 :dependencies '[[adzerk/boot-cljs          "1.7.48-6"   :scope "test"]
                 [adzerk/boot-cljs-repl     "0.2.0"      :scope "test"]
                 [adzerk/boot-reload        "0.4.1"      :scope "test"]
                 [pandeiro/boot-http        "0.6.3"      :scope "test"]

                 [navis/untangled-client "0.5.5"]
                 [org.clojure/clojurescript "1.8.51"]
                 [devcards "0.2.1-7"]
                 [devcards-om-next "0.3.0"]

                 [org.omcljs/om "1.0.0-alpha44"]

                 [garden "1.3.2"]
                 [sablono "0.7.4"]
                 [cljsjs/react "15.2.1-1"]
                 [cljsjs/react-dom "15.2.1-1"]

                 [org.clojure/core.async "0.2.374"]

                 [mathias/boot-sassc  "0.1.1" :scope "test"]])

(require
 '[adzerk.boot-cljs      :refer [cljs]]
 '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
 '[adzerk.boot-reload    :refer [reload]]
 '[pandeiro.boot-http    :refer [serve]]
 '[mathias.boot-sassc  :refer [sass]])

(deftask build []
  (comp (speak)
        (cljs )
        (sass :output-dir "css")))

(deftask run []
  (comp (serve)
        (watch)
        (cljs-repl)
        (reload)
        (build)))

(deftask production []
  (task-options! cljs {:optimizations :advanced}
                 sass   {:output-style "compressed"})
  identity)

(deftask development []
  (task-options! cljs {:optimizations :none
                       :source-map true}
                 reload {:on-jsload 'fourinarow.app/init}
                 sass   {:line-numbers true
                         :source-maps  true})
  identity)

(deftask dev
  "Simple alias to run application in development mode"
  []
  (comp (development)
        (run)))
