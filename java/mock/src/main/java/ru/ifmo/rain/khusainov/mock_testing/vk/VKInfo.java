package ru.ifmo.rain.khusainov.mock_testing.vk;

public class VKInfo {

    public final long id;
    public final long owner_id;
    public final long date;

    public VKInfo(long id, long owner_id, long date) {
        this.id = id;
        this.owner_id = owner_id;
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VKInfo vkInfo = (VKInfo) o;

        if (vkInfo.owner_id != owner_id) return false;
        return vkInfo.id == id;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = id;
        result = (int) (temp ^ (temp >>> 32));
        temp = owner_id;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = date;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public long getDate() {
        return date;
    }
}
