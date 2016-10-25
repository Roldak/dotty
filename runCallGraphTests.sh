#!/bin/sh

callSite=`pwd`

function execAtCallSite() {
  pushd $callSite > /dev/null
  $@
  popd > /dev/null
}

function findScalaSources() {
  echo `(ls ../*.scala | xargs realpath) 2> /dev/null`
}

function findJavaSources() {
  echo `(ls ../*.java | xargs realpath) 2> /dev/null`
}

function findAuxJavaSources() {
  echo `(ls ../java/*.java | xargs realpath) 2> /dev/null`
}

function compileScalaAndMoveGraph() {
  execAtCallSite $sc_cmp_cmd $1 && execAtCallSite mv "out3.dot" $2;
}

function compile() {
  sc_srcs=`findScalaSources`
  jv_srcs=`findJavaSources`
  jv_aux_srcs=`findAuxJavaSources`
  
  # if there are java sources but no java compilation command, issue a warning
  if ([ -n "$jv_srcs" ] || [ -n "$jv_aux_srcs" ]) && [ -z "$jv_cmp_cmd" ]; then
    echo "warning: java sources were found ($jv_srcs) but the java compilation command was no specified (use option -j)" 1>&2
  fi
  
  # if there are java sources and a java compilation command is given
  if [ -n "$jv_srcs" ] && [ -n "$jv_cmp_cmd" ]; then
    ( compileScalaAndMoveGraph "$sc_srcs $jv_srcs" $1 && execAtCallSite $jv_cmp_cmd $jv_srcs ) > compile-log.txt 2> compile-errors.txt || return 1
  else
    compileScalaAndMoveGraph "$sc_srcs" $1 > compile-log.txt 2> compile-errors.txt || return 1
  fi
  
  # if there are auxiliary java sources and a java compilation command is given
  if [ -n "$jv_aux_srcs" ] && [ -n "$jv_cmp_cmd" ]; then
    execAtCallSite $jv_cmp_cmd $jv_aux_srcs > compile-log.txt 2> compile-errors.txt || return 1
  fi
}

function run() {
  execAtCallSite $run_cmd Test > run-log.txt 2> run-errors.txt
}

if [ $# -ge 2 ] && [ $1 == "-j" ]; then
  jv_cmp_cmd="$2"; shift; shift
else
  jv_cmp_cmd=""
fi

if [ $# -lt 2 ]; then
  echo "Usage: `basename $0` [-j javaCompileCmd] scalaCompileCmd runCmd [test1 test2 ...]" 1>&2
  exit 1
else
  sc_cmp_cmd="$1"; shift
  run_cmd="$1"; shift
fi

if [ $# -eq 0 ]; then
  test_dirs=tests/callgraph/*
else
  test_dirs=$@
fi

code=0

for test_dir in $test_dirs; do
  echo $test_dir
  pushd $test_dir > /dev/null
  rm -R out > /dev/null 2> /dev/null
  mkdir -p out
  cd out > /dev/null
  compile "$test_dir/out/" && run || { echo "... failed"; code=1; }
  popd > /dev/null
  diff -y $test_dir/out/run-log.txt $test_dir/check.txt > $test_dir/out/run-log-diff.txt || { echo "... diff failed"; cat $test_dir/out/run-log-diff.txt; code=1; }
done

if [ $code -eq 0 ]; then
  exit 1
fi
