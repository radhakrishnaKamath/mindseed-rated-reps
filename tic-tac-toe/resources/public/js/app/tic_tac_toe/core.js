// Compiled by ClojureScript 1.9.671 {}
goog.provide('tic_tac_toe.core');
goog.require('cljs.core');
/**
 * Get all the values from board
 */
tic_tac_toe.core.get_map = (function tic_tac_toe$core$get_map(){
var iter__7775__auto__ = (function tic_tac_toe$core$get_map_$_iter__8816(s__8817){
return (new cljs.core.LazySeq(null,(function (){
var s__8817__$1 = s__8817;
while(true){
var temp__4657__auto__ = cljs.core.seq.call(null,s__8817__$1);
if(temp__4657__auto__){
var xs__5205__auto__ = temp__4657__auto__;
var i = cljs.core.first.call(null,xs__5205__auto__);
var iterys__7771__auto__ = ((function (s__8817__$1,i,xs__5205__auto__,temp__4657__auto__){
return (function tic_tac_toe$core$get_map_$_iter__8816_$_iter__8818(s__8819){
return (new cljs.core.LazySeq(null,((function (s__8817__$1,i,xs__5205__auto__,temp__4657__auto__){
return (function (){
var s__8819__$1 = s__8819;
while(true){
var temp__4657__auto____$1 = cljs.core.seq.call(null,s__8819__$1);
if(temp__4657__auto____$1){
var s__8819__$2 = temp__4657__auto____$1;
if(cljs.core.chunked_seq_QMARK_.call(null,s__8819__$2)){
var c__7773__auto__ = cljs.core.chunk_first.call(null,s__8819__$2);
var size__7774__auto__ = cljs.core.count.call(null,c__7773__auto__);
var b__8821 = cljs.core.chunk_buffer.call(null,size__7774__auto__);
if((function (){var i__8820 = (0);
while(true){
if((i__8820 < size__7774__auto__)){
var j = cljs.core._nth.call(null,c__7773__auto__,i__8820);
var prod = document.getElementById([cljs.core.str.cljs$core$IFn$_invoke$arity$1(i),cljs.core.str.cljs$core$IFn$_invoke$arity$1(j)].join('')).value;
cljs.core.chunk_append.call(null,b__8821,prod);

var G__8822 = (i__8820 + (1));
i__8820 = G__8822;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons.call(null,cljs.core.chunk.call(null,b__8821),tic_tac_toe$core$get_map_$_iter__8816_$_iter__8818.call(null,cljs.core.chunk_rest.call(null,s__8819__$2)));
} else {
return cljs.core.chunk_cons.call(null,cljs.core.chunk.call(null,b__8821),null);
}
} else {
var j = cljs.core.first.call(null,s__8819__$2);
var prod = document.getElementById([cljs.core.str.cljs$core$IFn$_invoke$arity$1(i),cljs.core.str.cljs$core$IFn$_invoke$arity$1(j)].join('')).value;
return cljs.core.cons.call(null,prod,tic_tac_toe$core$get_map_$_iter__8816_$_iter__8818.call(null,cljs.core.rest.call(null,s__8819__$2)));
}
} else {
return null;
}
break;
}
});})(s__8817__$1,i,xs__5205__auto__,temp__4657__auto__))
,null,null));
});})(s__8817__$1,i,xs__5205__auto__,temp__4657__auto__))
;
var fs__7772__auto__ = cljs.core.seq.call(null,iterys__7771__auto__.call(null,cljs.core.range.call(null,(1),(4))));
if(fs__7772__auto__){
return cljs.core.concat.call(null,fs__7772__auto__,tic_tac_toe$core$get_map_$_iter__8816.call(null,cljs.core.rest.call(null,s__8817__$1)));
} else {
var G__8823 = cljs.core.rest.call(null,s__8817__$1);
s__8817__$1 = G__8823;
continue;
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__7775__auto__.call(null,cljs.core.range.call(null,(1),(4)));
});
/**
 * Resets the board
 */
tic_tac_toe.core.reset = (function tic_tac_toe$core$reset(){
alert("play again");

return cljs.core.doall.call(null,(function (){var iter__7775__auto__ = (function tic_tac_toe$core$reset_$_iter__8824(s__8825){
return (new cljs.core.LazySeq(null,(function (){
var s__8825__$1 = s__8825;
while(true){
var temp__4657__auto__ = cljs.core.seq.call(null,s__8825__$1);
if(temp__4657__auto__){
var xs__5205__auto__ = temp__4657__auto__;
var i = cljs.core.first.call(null,xs__5205__auto__);
var iterys__7771__auto__ = ((function (s__8825__$1,i,xs__5205__auto__,temp__4657__auto__){
return (function tic_tac_toe$core$reset_$_iter__8824_$_iter__8826(s__8827){
return (new cljs.core.LazySeq(null,((function (s__8825__$1,i,xs__5205__auto__,temp__4657__auto__){
return (function (){
var s__8827__$1 = s__8827;
while(true){
var temp__4657__auto____$1 = cljs.core.seq.call(null,s__8827__$1);
if(temp__4657__auto____$1){
var s__8827__$2 = temp__4657__auto____$1;
if(cljs.core.chunked_seq_QMARK_.call(null,s__8827__$2)){
var c__7773__auto__ = cljs.core.chunk_first.call(null,s__8827__$2);
var size__7774__auto__ = cljs.core.count.call(null,c__7773__auto__);
var b__8829 = cljs.core.chunk_buffer.call(null,size__7774__auto__);
if((function (){var i__8828 = (0);
while(true){
if((i__8828 < size__7774__auto__)){
var j = cljs.core._nth.call(null,c__7773__auto__,i__8828);
cljs.core.chunk_append.call(null,b__8829,(function (){
document.getElementById([cljs.core.str.cljs$core$IFn$_invoke$arity$1(i),cljs.core.str.cljs$core$IFn$_invoke$arity$1(j)].join('')).value = "";

return document.getElementById([cljs.core.str.cljs$core$IFn$_invoke$arity$1(i),cljs.core.str.cljs$core$IFn$_invoke$arity$1(j)].join('')).disabled = false;
})()
);

var G__8830 = (i__8828 + (1));
i__8828 = G__8830;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons.call(null,cljs.core.chunk.call(null,b__8829),tic_tac_toe$core$reset_$_iter__8824_$_iter__8826.call(null,cljs.core.chunk_rest.call(null,s__8827__$2)));
} else {
return cljs.core.chunk_cons.call(null,cljs.core.chunk.call(null,b__8829),null);
}
} else {
var j = cljs.core.first.call(null,s__8827__$2);
return cljs.core.cons.call(null,(function (){
document.getElementById([cljs.core.str.cljs$core$IFn$_invoke$arity$1(i),cljs.core.str.cljs$core$IFn$_invoke$arity$1(j)].join('')).value = "";

return document.getElementById([cljs.core.str.cljs$core$IFn$_invoke$arity$1(i),cljs.core.str.cljs$core$IFn$_invoke$arity$1(j)].join('')).disabled = false;
})()
,tic_tac_toe$core$reset_$_iter__8824_$_iter__8826.call(null,cljs.core.rest.call(null,s__8827__$2)));
}
} else {
return null;
}
break;
}
});})(s__8825__$1,i,xs__5205__auto__,temp__4657__auto__))
,null,null));
});})(s__8825__$1,i,xs__5205__auto__,temp__4657__auto__))
;
var fs__7772__auto__ = cljs.core.seq.call(null,iterys__7771__auto__.call(null,cljs.core.range.call(null,(1),(4))));
if(fs__7772__auto__){
return cljs.core.concat.call(null,fs__7772__auto__,tic_tac_toe$core$reset_$_iter__8824.call(null,cljs.core.rest.call(null,s__8825__$1)));
} else {
var G__8831 = cljs.core.rest.call(null,s__8825__$1);
s__8825__$1 = G__8831;
continue;
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__7775__auto__.call(null,cljs.core.range.call(null,(1),(4)));
})());
});
/**
 * Get value of block from given position
 */
tic_tac_toe.core.get_val = (function tic_tac_toe$core$get_val(x,y){
return document.getElementById([cljs.core.str.cljs$core$IFn$_invoke$arity$1(x),cljs.core.str.cljs$core$IFn$_invoke$arity$1(y)].join('')).value;
});
/**
 * Checks for win by diag
 */
tic_tac_toe.core.check_diag_QMARK_ = (function tic_tac_toe$core$check_diag_QMARK_(a){
if((cljs.core._EQ_.call(null,tic_tac_toe.core.get_val.call(null,(1),(1)),tic_tac_toe.core.get_val.call(null,(2),(2)),tic_tac_toe.core.get_val.call(null,(3),(3)))) || (cljs.core._EQ_.call(null,tic_tac_toe.core.get_val.call(null,(1),(3)),tic_tac_toe.core.get_val.call(null,(2),(2)),tic_tac_toe.core.get_val.call(null,(3),(1))))){
if(cljs.core._EQ_.call(null,tic_tac_toe.core.get_val.call(null,(2),(2)),"X")){
return (1);
} else {
if(cljs.core._EQ_.call(null,tic_tac_toe.core.get_val.call(null,(2),(2)),"O")){
return (2);
} else {
return null;
}
}
} else {
return null;
}
});
/**
 * Checks for win by column
 */
tic_tac_toe.core.check_col_QMARK_ = (function tic_tac_toe$core$check_col_QMARK_(a){
if(cljs.core._EQ_.call(null,tic_tac_toe.core.get_val.call(null,(1),a),tic_tac_toe.core.get_val.call(null,(2),a),tic_tac_toe.core.get_val.call(null,(3),a))){
if(cljs.core._EQ_.call(null,tic_tac_toe.core.get_val.call(null,(1),a),"X")){
return (1);
} else {
if(cljs.core._EQ_.call(null,tic_tac_toe.core.get_val.call(null,(1),a),"O")){
return (2);
} else {
return null;
}
}
} else {
return null;
}
});
/**
 * Checks for win by row
 */
tic_tac_toe.core.check_row_QMARK_ = (function tic_tac_toe$core$check_row_QMARK_(a){
if(cljs.core._EQ_.call(null,tic_tac_toe.core.get_val.call(null,a,(1)),tic_tac_toe.core.get_val.call(null,a,(2)),tic_tac_toe.core.get_val.call(null,a,(3)))){
if(cljs.core._EQ_.call(null,tic_tac_toe.core.get_val.call(null,a,(1)),"X")){
return (1);
} else {
if(cljs.core._EQ_.call(null,tic_tac_toe.core.get_val.call(null,a,(1)),"O")){
return (2);
} else {
return null;
}
}
} else {
return null;
}
});
/**
 * Checks for each row column and diag
 */
tic_tac_toe.core.win_QMARK_ = (function tic_tac_toe$core$win_QMARK_(a){
var or__6986__auto__ = tic_tac_toe.core.check_row_QMARK_.call(null,a);
if(cljs.core.truth_(or__6986__auto__)){
return or__6986__auto__;
} else {
var or__6986__auto____$1 = tic_tac_toe.core.check_diag_QMARK_.call(null,a);
if(cljs.core.truth_(or__6986__auto____$1)){
return or__6986__auto____$1;
} else {
return tic_tac_toe.core.check_col_QMARK_.call(null,a);
}
}
});
/**
 * Checks if player won
 */
tic_tac_toe.core.check = (function tic_tac_toe$core$check(){
return cljs.core.first.call(null,cljs.core.remove.call(null,cljs.core.nil_QMARK_,cljs.core.doall.call(null,(function (){var iter__7775__auto__ = (function tic_tac_toe$core$check_$_iter__8832(s__8833){
return (new cljs.core.LazySeq(null,(function (){
var s__8833__$1 = s__8833;
while(true){
var temp__4657__auto__ = cljs.core.seq.call(null,s__8833__$1);
if(temp__4657__auto__){
var s__8833__$2 = temp__4657__auto__;
if(cljs.core.chunked_seq_QMARK_.call(null,s__8833__$2)){
var c__7773__auto__ = cljs.core.chunk_first.call(null,s__8833__$2);
var size__7774__auto__ = cljs.core.count.call(null,c__7773__auto__);
var b__8835 = cljs.core.chunk_buffer.call(null,size__7774__auto__);
if((function (){var i__8834 = (0);
while(true){
if((i__8834 < size__7774__auto__)){
var i = cljs.core._nth.call(null,c__7773__auto__,i__8834);
cljs.core.chunk_append.call(null,b__8835,tic_tac_toe.core.win_QMARK_.call(null,i));

var G__8836 = (i__8834 + (1));
i__8834 = G__8836;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons.call(null,cljs.core.chunk.call(null,b__8835),tic_tac_toe$core$check_$_iter__8832.call(null,cljs.core.chunk_rest.call(null,s__8833__$2)));
} else {
return cljs.core.chunk_cons.call(null,cljs.core.chunk.call(null,b__8835),null);
}
} else {
var i = cljs.core.first.call(null,s__8833__$2);
return cljs.core.cons.call(null,tic_tac_toe.core.win_QMARK_.call(null,i),tic_tac_toe$core$check_$_iter__8832.call(null,cljs.core.rest.call(null,s__8833__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__7775__auto__.call(null,cljs.core.range.call(null,(1),(4)));
})())));
});
/**
 * Takes the block pos from user
 */
tic_tac_toe.core.mark = (function tic_tac_toe$core$mark(no){
if(cljs.core._EQ_.call(null,tic_tac_toe.core.check.call(null),(1))){
alert("X won");

return tic_tac_toe.core.reset.call(null);
} else {
if(cljs.core._EQ_.call(null,tic_tac_toe.core.check.call(null),(2))){
alert("O won");

return tic_tac_toe.core.reset.call(null);
} else {
if(cljs.core._EQ_.call(null,(9),cljs.core.count.call(null,cljs.core.filter.call(null,(function (p1__8837_SHARP_){
return !(cljs.core.empty_QMARK_.call(null,p1__8837_SHARP_));
}),tic_tac_toe.core.get_map.call(null))))){
return tic_tac_toe.core.reset.call(null);
} else {
if(cljs.core.even_QMARK_.call(null,cljs.core.count.call(null,cljs.core.filter.call(null,(function (p1__8838_SHARP_){
return !(cljs.core.empty_QMARK_.call(null,p1__8838_SHARP_));
}),tic_tac_toe.core.get_map.call(null))))){
document.getElementById(no).value = "X ";

return document.getElementById(no).disabled = true;
} else {
document.getElementById(no).value = "O";

return document.getElementById(no).disabled = true;
}
}
}
}
});

//# sourceMappingURL=core.js.map