package hibernateUtils.hibernateObjects;

import org.springframework.beans.BeanUtils;

public abstract class MalMapping {
    public void update(MalMapping mapping) {
        if (!(this.getClass().isInstance(mapping))) {
            return;
        }

        BeanUtils.copyProperties(mapping, this);
    }
}
