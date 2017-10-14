package com.xurpas.integration.ui.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.xurpas.integration.R
import java.util.ArrayList

/**
 * Created by Emmanuel Victor Garcia on 10/14/17.
 */
class SceneAdapter(var context: Context, var steps: ArrayList<String>): RecyclerView.Adapter<SceneAdapter.SceneHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SceneHolder {
        return SceneHolder(LayoutInflater.from(context).inflate(R.layout.item_step, parent, false))
    }

    override fun getItemCount(): Int {
        return steps.size
    }

    override fun onBindViewHolder(holder: SceneHolder, position: Int) {
        holder.tvStep.text = steps[position]
    }

    class SceneHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvStep: TextView = itemView.findViewById(R.id.tv_step)
    }

}