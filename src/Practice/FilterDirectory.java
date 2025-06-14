package Practice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FilterDirectory {
    public static void main(String[] args) {
        Filter nameFilter = new NameFilter("file");
        Filter sizeFilter = new SizeFilter(100, 10000);
        Filter fullFilter = new AndFilter(List.of(nameFilter, sizeFilter));

        FileSearcher searcher = new FileSearcher();
        Directory directory = new Directory("files", 5000);
        directory.children.add(new File("file1", 100));
        directory.children.add(new File("file2", 300));
        directory.children.add(new File("file3", 3000));
        directory.children.add(new File("report", 500));
        List<FileEntity> results = searcher.filter(directory, fullFilter);
        System.out.println(results);

    }
}
abstract class FileEntity {
    String name;
    long size;
    Date createdAt, modifiedAt;

    abstract boolean isDirectory();

    @Override
    public String toString() {
        return "FileEntity{" +
                "name='" + name + '\'' +
                '}';
    }
}

class File extends FileEntity {
    public File(String name, long size) {
        this.name = name;
        this.size = size;
    }
    boolean isDirectory() { return false; }
}

class Directory extends FileEntity {
    List<FileEntity> children;
    public Directory(String name, long size) {
        this.name = name;
        this.size = size;
        children = new ArrayList<>();
    }
    boolean isDirectory() { return true; }
}

interface Filter {
    boolean matches(FileEntity file);
}

class NameFilter implements Filter {
    String keyword;

    public NameFilter(String keyword) {
        this.keyword = keyword;
    }

    public boolean matches(FileEntity f) {
        return f.name.contains(keyword);
    }
}

class ExtensionFilter implements Filter {
    String ext;
    public boolean matches(FileEntity f) {
        return f instanceof File && f.name.endsWith(ext);
    }
}

class SizeFilter implements Filter {
    long minSize;
    long maxSize;

    public SizeFilter(long minSize, long maxSize) {
        this.minSize = minSize;
        this.maxSize = maxSize;
    }

    public boolean matches(FileEntity f) {
        return f.size >= minSize && f.size <= maxSize;
    }
}

class AndFilter implements Filter {
    List<Filter> filters;

    public AndFilter(List<Filter> filters) {
        this.filters = filters;
    }

    public boolean matches(FileEntity f) {
        for (Filter filter : filters)
            if (!filter.matches(f)) return false;
        return true;
    }
}

class OrFilter implements Filter {
    List<Filter> filters;
    public boolean matches(FileEntity f) {
        for (Filter filter : filters)
            if (filter.matches(f)) return true;
        return false;
    }
}

class FileSearcher {

    List<FileEntity> filter(FileEntity root, Filter filter) {
        List<FileEntity> result = new ArrayList<>();
        dfs(root, filter, result);
        return result;
    }

    private void dfs(FileEntity current, Filter filter, List<FileEntity> result) {
        if (filter.matches(current))
            result.add(current);

        if (current.isDirectory()) {
            Directory dir = (Directory) current;
            for (FileEntity child : dir.children)
                dfs(child, filter, result);
        }
    }
}


