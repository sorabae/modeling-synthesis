package fr.inria.gforge.spoon.analysis;


public class ReferenceProcessor extends spoon.processing.AbstractProcessor<spoon.reflect.declaration.CtPackage> {
    private java.util.List<spoon.reflect.reference.CtTypeReference<?>> ignoredTypes = new java.util.ArrayList<spoon.reflect.reference.CtTypeReference<?>>();

    public java.util.List<java.util.List<spoon.reflect.reference.CtPackageReference>> circularPathes = new java.util.ArrayList<java.util.List<spoon.reflect.reference.CtPackageReference>>();

    @java.lang.Override
    public void init() {
        ignoredTypes.add(getFactory().Type().createReference(spoon.compiler.Environment.class));
        ignoredTypes.add(getFactory().Type().createReference(spoon.reflect.factory.Factory.class));
        ignoredTypes.add(getFactory().Type().createReference(spoon.processing.FactoryAccessor.class));
    }

    java.util.Map<spoon.reflect.reference.CtPackageReference, java.util.Set<spoon.reflect.reference.CtPackageReference>> packRefs = new java.util.HashMap<spoon.reflect.reference.CtPackageReference, java.util.Set<spoon.reflect.reference.CtPackageReference>>();

    public void process(spoon.reflect.declaration.CtPackage element) {
        spoon.reflect.reference.CtPackageReference pack = element.getReference();
        java.util.Set<spoon.reflect.reference.CtPackageReference> refs = new java.util.HashSet<spoon.reflect.reference.CtPackageReference>();
        for (spoon.reflect.declaration.CtType t : element.getTypes()) {
            java.util.List<spoon.reflect.reference.CtTypeReference<?>> listReferences = spoon.reflect.visitor.Query.getReferences(t, new spoon.reflect.visitor.filter.ReferenceTypeFilter<spoon.reflect.reference.CtTypeReference<?>>(spoon.reflect.reference.CtTypeReference.class));
            for (spoon.reflect.reference.CtTypeReference<?> tref : listReferences) {
                if (((tref.getPackage()) != null) && (!(tref.getPackage().equals(pack)))) {
                    if (ignoredTypes.contains(tref))
                        continue;
                    
                    refs.add(tref.getPackage());
                }
            }
        }
        if ((refs.size()) > 0) {
            packRefs.put(pack, refs);
        }
    }

    @java.lang.Override
    public void processingDone() {
        for (spoon.reflect.reference.CtPackageReference p : packRefs.keySet()) {
            java.util.Stack<spoon.reflect.reference.CtPackageReference> path = new java.util.Stack<spoon.reflect.reference.CtPackageReference>();
            path.push(p);
            scanDependencies(path);
        }
    }

    java.util.Set<spoon.reflect.reference.CtPackageReference> scanned = new java.util.HashSet<spoon.reflect.reference.CtPackageReference>();

    void scanDependencies(java.util.Stack<spoon.reflect.reference.CtPackageReference> path) {
        spoon.reflect.reference.CtPackageReference ref = path.peek();
        if (scanned.contains(ref)) {
            return ;
        }
        scanned.add(ref);
        java.util.Set<spoon.reflect.reference.CtPackageReference> refs = packRefs.get(ref);
        if (refs != null) {
            for (spoon.reflect.reference.CtPackageReference p : refs) {
                if (path.contains(p)) {
                    java.util.List<spoon.reflect.reference.CtPackageReference> circularPath = new java.util.ArrayList<spoon.reflect.reference.CtPackageReference>(path.subList(path.indexOf(p), path.size()));
                    circularPath.add(p);
                    circularPathes.add(circularPath);
                    break;
                }else {
                    path.push(p);
                    scanDependencies(path);
                    path.pop();
                }
            }
        }
    }
}

