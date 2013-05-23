zipwhip-framework
=================

Application framework. Useful for developing applications that have loosely coupled services using a plugin model.

The design is to divide your different pieces of functionality into units called a "Feature"

Features are designed to be pluggable. They do not have references to each other directly. They share a common event bus (called a Broker).

    public class MyFeature extends Feature {
      
      /**
       * @param uri The actual URI that was called.
       * @param e The event data.
       */
      @Subscribe(uri = "/signal/message/receive")
      public void process(String uri, EventData e) {
        // This method is called in the core Broker thread
      }
    }
