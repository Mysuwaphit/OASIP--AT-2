package sit.int221.projectintegrate;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class listMapper {
    private static final listMapper listMapper = new listMapper();
    private listMapper() { }
    public <S, T> List<T> mapList(List<S> source, Class<T> targetClass, ModelMapper modelMapper) {
        return source.stream().map(entity -> modelMapper.map(entity, targetClass))
                .collect(Collectors.toList());
    }
    public static listMapper getInstance() {
        return listMapper;
    }
}
//    public <S,  T> PageDTO<T> toPageDTO(Page<S> source, Class<T> targetClass,ModelMappermodelMapper)
//    {PageDTO<T> page = modelMapper.map(source, PageDTO.class);page.setEntities(mapList(source.getContent(), targetClass,  modelMapper));return page;}}
