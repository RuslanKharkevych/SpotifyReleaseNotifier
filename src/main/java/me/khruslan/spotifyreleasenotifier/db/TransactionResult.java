package me.khruslan.spotifyreleasenotifier.db;

public interface TransactionResult<T> {
    boolean isSuccess();
    T getOrDefault(T defaultValue);

    record Success<T>(T value) implements TransactionResult<T> {
        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public T getOrDefault(T defaultValue) {
            return value;
        }
    }

    record Error<T>() implements TransactionResult<T> {
        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public T getOrDefault(T defaultValue) {
            return defaultValue;
        }
    }
}
