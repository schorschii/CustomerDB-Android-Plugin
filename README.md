# Customer Database Android Plugin
[![APK Download](.github/apk-badge.svg)](https://github.com/schorschii/CustomerDB-Android-Plugin/releases)

The plugin app shows the name of a customer in the [Customer Database App for Android](https://github.com/schorschii/CustomerDB-Android) on incoming calls on devices without the Google Phone app.

Please head over to [Releases](https://github.com/schorschii/CustomerDB-Android-Plugin/releases) to download the latest APK file.

Most users are using the Google Dialer app, which queries 3rd party apps (like the Customer Database) for caller ID via `android.content.ContactDirectory`. This allows the Customer Database app to natively show customer names on the screen of incoming calls.

This plugin app displays the customer name on incoming calls on devices without the Google phone app (such as Samsung, HTC etc. which ship their own phone apps). Of course, on the first startup, you need to grant the plugin app permission to view incoming calls. When your phone gets called, the plugin app will be noticed by Android and search the customer database for the incoming number. If a match was found, it displays the customer name on a small overlay over the incoming call.

This functionality must be outsourced into a separate app as the main app is not allowed to have the `READ_PHONE_STATE` and `READ_CALL_LOG` permission in the Play Store.

The plugin app was previoulsy included in the Customer Database app but since end of 2022, Google enforced new policies in the Play Store which denies the usage of the `REQUEST_INSTALL_PACKAGES` permission on apps which are not app stores itself. This means that the Customer Database app cannot install the plugin app by itself (which was always done with user consent, of course).
