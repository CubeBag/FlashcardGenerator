cc=$(< commit_count)
let cc++
echo $cc > commit_count
git add *
git commit -m $cc
git push
