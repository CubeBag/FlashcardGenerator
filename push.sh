git add *
cc=$(< commit_count)
let cc++
echo $cc > commit_count
git commit -m $cc
git push
