# Common issues

## BeanUtils.copyProperties doesn't work
Make sure you use the correct BeanUtils
- `org.springframework.beans.BeanUtils.copyProperties` requires your class with getter/setter
- `org.apache.commons.beanutils.BeanUtils.copyProperties` does not require getter/setter - use reflection and more powerful

