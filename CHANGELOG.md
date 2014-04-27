### 0.2.1
- Changed package name to uk.org.eastoe.duncan.itg.api.*

### 0.2
- Introduced ITGMessage.Type to represent message types
- Improved JavaDoc
- ITGMessage.getSender() now returns an InetAddress object
- ITGApi.sendCmd(...) is now overloaded - sender can be passed as a String or InetAddress
- Added ITGMessage.equals(...)
- Changed various constant visibilities
- New exceptions thrown
- Unit testing

### 0.1
- Initial release
- Imported from ITGController
