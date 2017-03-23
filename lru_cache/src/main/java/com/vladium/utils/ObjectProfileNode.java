
package com.vladium.utils;

import java.util.Arrays;
   
// ----------------------------------------------------------------------------
/**
 * A non-shell profile tree node implementation. This implementation trades off
 * some object orientation "niceness" to achieve more memory compactness.
 * 
 * @author (C) <a href="http://www.javaworld.com/columns/jw-qna-index.shtml">Vlad Roubtsov</a>, 2003
 */
final class ObjectProfileNode extends AbstractProfileNode
{
    // public: ................................................................    

    public Object object ()
    {
        return m_obj;
    }
    
    public String name ()
    {
        return m_link == null ? ObjectProfiler.INPUT_OBJECT_NAME : m_link.name ();
    }

    public IObjectProfileNode shell ()
    {
        return m_shell;
    }
    
    public IObjectProfileNode [] children ()
    {
        return m_children;
    }

    public int refcount ()
    {
        return m_refcount;
    }
    
    public boolean traverse (final INodeFilter filter, final INodeVisitor visitor)
    {
        if ((visitor != null) && ((filter == null) || filter.accept (this)))
        {
            visitor.previsit (this);
            
            final IObjectProfileNode [] children = m_children;
            for (int i = 0; i < children.length; ++ i)
            {
                children [i].traverse (filter, visitor);
            }
            
            visitor.postvisit (this);
            
            return true;
        }
        
        return false;
    }
            
    // protected: .............................................................

    // package: ...............................................................
    
    /*
     * This method manages the vector in m_children field for an unfinished node.
     */
    void addFieldRef (final IObjectProfileNode node)
    {
        // [m_size is the child count]
        
        IObjectProfileNode [] children = m_children;
        final int childrenLength = children.length;
        if (m_size >= childrenLength)
        {
            final IObjectProfileNode [] newchildren = new IObjectProfileNode [Math.max (1, childrenLength << 1)];
            System.arraycopy (children, 0, newchildren, 0, childrenLength);
            m_children = children = newchildren;
        }
        children [m_size ++] = node;
    }
    
    /*
     * This method is called once on every node to lock it down into its
     * immutable and most compact representation during phase 2 of profile
     * tree construction.
     */
    void finish ()
    {
        final int childCount = m_size; // m_size is the child count for a non-shell node
        if (childCount > 0)
        {
            if (childCount < m_children.length)
            {
                final IObjectProfileNode [] newadj = new IObjectProfileNode [childCount];
                System.arraycopy (m_children, 0, newadj, 0, childCount);
                
                m_children = newadj;
            }
            
            Arrays.sort (m_children);
            
            int size = 0;
            for (int i = 0; i < childCount; ++ i)
            {
                size += m_children [i].size ();
            }
            m_size = size; // m_size is the full node size for all nodes
        }
    }
    
    
    ObjectProfileNode (final ObjectProfileNode parent, final Object obj, final ILink link)
    {
        super (parent);

        m_obj = obj;
        m_link = link;
        m_refcount = 1;
        m_children = EMPTY_OBJECTPROFILENODE_ARRAY;
    }


    final ILink m_link;
    final Object m_obj;
    int m_refcount;
    AbstractShellProfileNode m_shell;
    IObjectProfileNode [] m_children;
        
    // private: ...............................................................

} // end of class
// ----------------------------------------------------------------------------