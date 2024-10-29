# local deploy script for the web front-end

# This file is responsible for preprocessing all TypeScript files, making sure
# all dependencies are up-to-date, and copying all necessary files into a
# local web deploy directory, and starting a web server

# This is the resource folder where maven expects to find our files
TARGETFOLDER=../test-backend/src/main/resources

# This is the folder that we used when configuring Javalin in Javalin.create()
WEBFOLDERNAME=public

# step 1: make sure we have someplace to put everything.  We will delete the
#         old folder tree, and then make it from scratch
echo "deleting $TARGETFOLDER and creating an empty $TARGETFOLDER/$WEBFOLDERNAME"
rm -rf $TARGETFOLDER
mkdir -p $TARGETFOLDER/$WEBFOLDERNAME

# step 2: update our npm dependencies
#echo "Updating node dependencies"
#npm update

# step 3: build the react app
echo "Building React app"
npm run build

# step 3: copy static html, css, and JavaScript files
# echo "Copying static html, css, and js files"
# cp -r src $TARGETFOLDER
# cp -r public $TARGETFOLDER
echo "Copying static build files"
cp -r ./build $TARGETFOLDER/$WEBFOLDERNAME

# step final: launch the server.  Be sure to disable caching
# (Note: we don't currently use -s for silent operation)
# echo "Starting local webserver at $TARGETFOLDER/$WEBFOLDERNAME"
# npm start $TARGETFOLDER/$WEBFOLDERNAME