// Compiled by ClojureScript 1.9.671 {}
goog.provide('first_cljs.core');
goog.require('cljs.core');
goog.require('clojure.string');
first_cljs.core.crypt_js = (function first_cljs$core$crypt_js(stri,k){
var sep_str = cljs.core.partition.call(null,(cljs.core.count.call(null,stri) / cljs.core.count.call(null,k)),cljs.core.js__GT_clj.call(null,cljs.core.clj__GT_js.call(null,stri).split("")));
var sep_key = cljs.core.js__GT_clj.call(null,cljs.core.clj__GT_js.call(null,k).split(""));
return cljs.core.apply.call(null,cljs.core.str,cljs.core.flatten.call(null,cljs.core.vals.call(null,cljs.core.sort.call(null,cljs.core.zipmap.call(null,sep_key,sep_str)))));
});
first_cljs.core.crypt = (function first_cljs$core$crypt(stri,k){
var sep_str = cljs.core.partition.call(null,(cljs.core.count.call(null,stri) / cljs.core.count.call(null,k)),clojure.string.split.call(null,stri,""));
var sep_key = clojure.string.split.call(null,k,"");
return cljs.core.apply.call(null,cljs.core.str,cljs.core.flatten.call(null,cljs.core.vals.call(null,cljs.core.sort.call(null,cljs.core.zipmap.call(null,sep_key,sep_str)))));
});
/**
 * I don't do a whole lot ... yet.
 */
first_cljs.core.main = (function first_cljs$core$main(var_args){
var args__8118__auto__ = [];
var len__8111__auto___8688 = arguments.length;
var i__8112__auto___8689 = (0);
while(true){
if((i__8112__auto___8689 < len__8111__auto___8688)){
args__8118__auto__.push((arguments[i__8112__auto___8689]));

var G__8690 = (i__8112__auto___8689 + (1));
i__8112__auto___8689 = G__8690;
continue;
} else {
}
break;
}

var argseq__8119__auto__ = ((((0) < args__8118__auto__.length))?(new cljs.core.IndexedSeq(args__8118__auto__.slice((0)),(0),null)):null);
return first_cljs.core.main.cljs$core$IFn$_invoke$arity$variadic(argseq__8119__auto__);
});

first_cljs.core.main.cljs$core$IFn$_invoke$arity$variadic = (function (args){
var input = document.getElementById("input").value;
var input1 = document.getElementById("input1").value;
var output = first_cljs.core.crypt_js.call(null,input,input1);
return document.getElementById("output").value = output;
});

first_cljs.core.main.cljs$lang$maxFixedArity = (0);

first_cljs.core.main.cljs$lang$applyTo = (function (seq8687){
return first_cljs.core.main.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq.call(null,seq8687));
});


//# sourceMappingURL=core.js.map