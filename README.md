# AeroGear PushPlugin Cordova

[![npm package](https://nodei.co/npm/aerogear-cordova-push.png?downloads=true&downloadRank=true&stars=true)](https://nodei.co/npm/aerogear-cordova-push/)

[![Dependency Status](https://img.shields.io/david/aerogear/aerogear-cordova-push.svg?style=flat-square)](https://david-dm.org/aerogear/aerogear-cordova-push)
[![Known Vulnerabilities](https://snyk.io/test/npm/aerogear-cordova-push/badge.svg?style=flat-square)](https://snyk.io/test/npm/aerogear-cordova-push)

> This plugin makes starting with AeroGear Unified Push simple.

|                 | Project Info  |
| --------------- | ------------- |
| License:        | Apache License, Version 2.0  |
| Build:          | Cordova Plugin  |
| Documentation:  | https://aerogear.org/docs/specs/aerogear-cordova/  |
| Issue tracker:  | https://issues.jboss.org/browse/AGCORDOVA  |
| Mailing lists:  | [aerogear-users](http://aerogear-users.1116366.n5.nabble.com/) ([subscribe](https://lists.jboss.org/mailman/listinfo/aerogear-users))  |
|                 | [aerogear-dev](http://aerogear-dev.1069024.n5.nabble.com/) ([subscribe](https://lists.jboss.org/mailman/listinfo/aerogear-dev))  |

## Getting Started
* [AeroGear Push Plugin Guide](http://aerogear.org/docs/guides/aerogear-cordova/AerogearCordovaPush/)

## Run tests

1. Use your existing cordova app, or create a new one.
1. Add the plugin and the tests:

  ```bash
  cordova plugin add https://github.com/aerogear/aerogear-cordova-push.git
  cordova plugin add https://github.com/aerogear/aerogear-cordova-push.git#:/tests
  ```

3. Add this plugin:
  ```bash
  cordova plugin add http://git-wip-us.apache.org/repos/asf/cordova-plugin-test-framework.git
  ```
4. Change the start page in `config.xml` with `<content src="cdvtests/index.html" />` or navigate to cdvtests/index.html from within your app.

## Documentation

For more details about the current release, please consult [our documentation](https://aerogear.org/docs/specs/aerogear-cordova/).

## Android notification icon

To show a better notification icon in Android Lollipop and above, create a transparent icon and name the file as "icon_white.png" and put into platforms/android/res/drawable folder.

## Configuring Firebase Version

Many other plugins require Google Play Services and/or Firebase libraries. This is a common source of Android build-failures, since the library version must be aligned to the same version for all plugins. Use the `FIREBASE_VERSION` to align the required firebase-messaging version with other plugins.

For example:

```
cordova plugin add aerogear-cordova-push --variable FIREBASE_VERSION=18.0.0
```

## AndroidX Support

This plugin has [AndroidX](https://developer.android.com/jetpack/androidx) support. This means that you should migrate your project to AndroidX. To prevent to do it manually everytime, there are 2 great plugins to migrate it:

1. First, enable AndroidX adding the [cordova-plugin-androidx](https://github.com/dpa99c/cordova-plugin-androidx) plugin:

```
cordova plugin add cordova-plugin-androidx
```

2. If you encounter build failures after installing (or after manually enabling AndroidX), try to install [cordova-plugin-androidx-adapter](https://github.com/dpa99c/cordova-plugin-androidx-adapter) into your project. It will migrate any references from the legacy Android Support library to use the new AndroidX which should resolve build failures.
```
cordova plugin add cordova-plugin-androidx-adapter
```

## Development

If you would like to help develop AeroGear you can join our [developer's mailing list](https://lists.jboss.org/mailman/listinfo/aerogear-dev), join #aerogear on Freenode, or shout at us on Twitter @aerogears.

Also takes some time and skim the [contributor guide](http://aerogear.org/docs/guides/Contributing/)

## Questions?

Join our [user mailing list](https://lists.jboss.org/mailman/listinfo/aerogear-users) for any questions or help! We really hope you enjoy app development with AeroGear!

## Found a bug?

If you found a bug please create a ticket for us on [Jira](https://issues.jboss.org/browse/AGCORDOVA) with some steps to reproduce it.
