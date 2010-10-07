package net.sf.umlspeed.entities;

import java.util.ArrayList;
import java.util.List;

public class Deployment implements Entity {

        public final static int NODE = 0;
        public final static int DATABASE = 1;
    
        protected String name = "";
        protected String displayName = "";
        protected String platform = "";
        protected int type = NODE;
        protected List contents = new ArrayList();
        /**
         * @return the contents
         */
        public List getContents() {
            return contents;
        }
        /**
         * @param contents the contents to set
         */
        public void setContents(List contents) {
            this.contents = contents;
        }
        /**
         * @return the name
         */
        public String getName() {
            return name;
        }
        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }
        /**
         * @return the platform
         */
        public String getPlatform() {
            return platform;
        }
        /**
         * @param platform the platform to set
         */
        public void setPlatform(String platform) {
            this.platform = platform;
        }
        /**
         * @return the type
         */
        public int getType() {
            return type;
        }
        /**
         * @param type the type to set
         */
        public void setType(int type) {
            this.type = type;
        }
        /**
         * @return the displayName
         */
        public String getDisplayName() {
            return displayName;
        }
        /**
         * @param displayName the displayName to set
         */
        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }
        
        
   
}
