cc=$(< commit_count)
let cc++
echo $cc > commit_count
echo $cc
