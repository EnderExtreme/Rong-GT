package com.rong.rt.api.utils;

public interface IWeighted {

    int getWeight();

    class Wrapper<T> implements IWeighted {
        private final T object;
        private final int weight;

        public Wrapper(T object, int weight) {
            this.object = object;
            this.weight = weight;
        }

        public T getObject() {
            return object;
        }

        @Override
        public int getWeight() {
            return weight;
        }
    }
}
