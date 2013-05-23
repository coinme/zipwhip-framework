zipwhip-framework
=================

Application framework. Useful for developing applications that have loosely coupled services using a plugin model.

The design is to divide your different pieces of functionality into units called a "Feature"

Features are designed to be pluggable. They do not have references to each other directly. They share a common event bus (called a Broker).

Example
------------

This example feature will watch the global application event bus for a new message. When the new message is received, it will append it to the mailbox for storage.

    public class AppendToMailboxFeature extends Feature {
      
      @Autowired
      Mailbox mailbox;
      
      /**
       * This feature responds to 
       *
       * @param uri The actual URI that was called.
       * @param eventData The event data.
       */
      @Subscribe(uri = "/signal/message/receive")
      public void process(String uri, EventData eventData) {
        // This method is called in the core Broker thread pool.
        
        // pull out the first argument (eventData is an array of objects)
        // cast it to a message. If it's not a message type, silently return null.
        // If you don't want it to swallow the problem silently, grab it out of the array manually.
        Message message = EventDataUtil.getExtra(Message.class, eventData, 0);
        
        mailbox.append(message);        
      }
    }

Example configuration
------------

    public class Main {
        public static void main(String[] args) {
            // typically you would use Spring to create the application.
            Application application = new Application();
            
            // the Autowired annotation above will not function if you do not use Spring.
            application.addPlugin(new AppendToMailboxFeature());
            
            // the Application is a Plugin. Plugins must be initialized with their parent. 
            // The Application has no parent and thus is initialized with null.            
            application.init(null);
        }
    }
