Java-SafeBrowsing [![Build Status](https://travis-ci.org/kivibot/Java-SafeBrowsing.svg)](https://travis-ci.org/kivibot/Java-SafeBrowsing)
=================
A simple java wrapper for Google's safe browsing API v3.1

Only supports the lookup api at the moment!

```
SafeBrowsingLookup lookup = new SafeBrowsingLookup("key");
try {
	LookupResult result = lookup.lookupURL("http://google.com/");
	System.out.println(result.isTrusted());
} catch (IOException | ServiceUnavailableException | LookupException ex) {
	Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
}
```
