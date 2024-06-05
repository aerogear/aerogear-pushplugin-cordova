module.exports = function(ctx) {
    var fs = require('fs'),
        os = require("os"),
        readline = require("readline");

    var platformAndroid = 'platforms/android';

    if (fs.existsSync('platforms/android/app/src/main')) {
        // cordova-android >= 7.0.0
        platformAndroid = 'platforms/android/app';
    }

    return new Promise(function(resolve, reject) {
		var lineReader = readline.createInterface({
			terminal: false,
			input : fs.createReadStream(platformAndroid + '/build.gradle')
		});
		lineReader.on("line", function(line) {
			fs.appendFileSync('./build.gradle', line.toString() + os.EOL);
			if (/.*\ dependencies \{.*/.test(line)) {
				fs.appendFileSync('./build.gradle', '\t\tclasspath "com.google.gms:google-services:4.2.0"' + os.EOL);
				fs.appendFileSync('./build.gradle', '\t\tclasspath "com.android.tools.build:gradle:3.3.0"' + os.EOL);
			}
		}).on("close", function () {
			fs.rename('./build.gradle', platformAndroid + '/build.gradle', resolve);
		});
    })
};
