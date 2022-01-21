package com.example.whatsappclone;

public class contactsModel {

        String name,number;

        public contactsModel(){

        }

        public contactsModel(String name, String number) {
                this.name = name;
                this.number = number;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getNumber() {
                return number;
        }

        public void setNumber(String number) {
                this.number = number;
        }

}
