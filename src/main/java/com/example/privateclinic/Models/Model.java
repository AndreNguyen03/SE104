package com.example.privateclinic.Models;

import com.example.privateclinic.Views.ViewFactory;

public class Model {
        private static com.example.privateclinic.Models.Model model;
        private final ViewFactory viewFactory;
        private Model() {
            this.viewFactory = new ViewFactory();
        }
        public static synchronized com.example.privateclinic.Models.Model getInstance() {
            if(model == null) {
                model = new com.example.privateclinic.Models.Model();
            }
            return model;
        }
        public ViewFactory getViewFactory() {
            return viewFactory;
        }
}
