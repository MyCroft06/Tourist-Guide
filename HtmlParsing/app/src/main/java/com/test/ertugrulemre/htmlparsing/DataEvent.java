package com.test.ertugrulemre.htmlparsing;

/**
 * Created by mycroft on 2.2.2018.
 */

public class DataEvent {

        private String name;
        private String address;
        private String statement;

        public DataEvent(String name, String address, String statement) {
            this.name = name;
            this.address = address;
            this.statement = statement;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getStatement() {
            return statement;
        }

        public void setStatement(String statement) {
            this.statement = statement;
        }
    }